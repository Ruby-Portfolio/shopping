package ruby.shopping.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.account.exception.AccountUnauthorizedException;
import ruby.shopping.security.AccountDetails;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final Key key;
    @Value("${jwt.expireTime}")
    private Integer expireTime;
    public final AccountRepository accountRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            AccountRepository accountRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accountRepository = accountRepository;
    }

    /** 유저 이메일 정보를 가지고 Token 을 생성 */
    public String createToken(String email) {
        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + expireTime);
        return Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(tokenExpiresIn)
                .compact();
    }

    /** 토큰을 복호화하여 토큰에 들어있는 정보를 획득 */
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 토큰에서 얻은 이메일로 사용자 정보 조회
        String email = claims.getSubject();
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(AccountUnauthorizedException::new);

        List<GrantedAuthority> authorities = account.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.name()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(new AccountDetails(account, authorities), "", authorities);
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않은 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
