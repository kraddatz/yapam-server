package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.InvalidAccessTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RequestHelperService.class)
@ActiveProfiles("test")
class RequestHelperServiceTest extends YapamBaseTest {

    private final String validToken = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCIsImtpZCI6InhpdXNwMmp2Mkp2YTA0a0JGVFItZklEbmsxMnNTLVItU0NqWm5NdGQ2bm8ifQ.eyJqdGkiOiJkMDlmMGI4Zi04OTk1LTQ2NzAtYjU4NS1iYjlhYTQ3NWJjMjciLCJleHAiOjE1NjUzMDAxOTMsIm5iZiI6MCwiaWF0IjoxNTY1Mjk5ODkzLCJpc3MiOiIiLCJhdWQiOlsibW9iaWxlIiwiYWNjb3VudCJdLCJzdWIiOiJhNzQwMTlhYS1lOWQxLTQyMTEtOGM4OC1hZjFlZWI1MjE0MzciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3ZWIiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIxYmU0NjE4My05YjkyLTQ3ZDQtODkxMS04MmQ1ZDlmNzcxMjkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ3ZWIiOnsicm9sZXMiOlsidXNlciJdfSwibW9iaWxlIjp7InJvbGVzIjpbIlVTRVIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6Ik1heCBNdXN0ZXJtYW5uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlckBlbWFpbC5jb20iLCJnaXZlbl9uYW1lIjoiTWF4IiwiZmFtaWx5X25hbWUiOiJNdXN0ZXJtYW5uIiwiZW1haWwiOiJ1c2VyQGVtYWlsLmNvbSJ9.-4HKbDV5OtEx2zXoNq4yT8s-rrVeeC88blOhHp08fmvIa16UgoAai8WVPNdtGeE6Yocqx01vY4Z2BhzhYESotQ";
    private final String malformedtoken = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCIsImtpZCI6InhpdXNwMmp2Mkp2YTA0a0JGVFItZklEbmsxMnNTLVItU0NqWm5NdGQ2bm8ifQ.ewogICJqdGkiOiAiZDA5ZjBiOGYtODk5NS00NjcwLWI1ODUtYmI5YWE0NzViYzI3IiwKICAiZXhwIjogMTU2NTMwMDE5MywKICAibmJmIjogMCwKICAiaWF0IjogMTU2NTI5OTg5MywKICAiaXNzIjogIiIsCiAgImF1ZCI6IFsKICAgICJ0ZXN0IiA6ICJtb2JpbGUiLAogICAgImFjY291bnQiCiAgXQp9.-4HKbDV5OtEx2zXoNq4yT8s-rrVeeC88blOhHp08fmvIa16UgoAai8WVPNdtGeE6Yocqx01vY4Z2BhzhYESotQ";

    @Autowired private RequestHelperService requestHelperService;

    @Test
    void whenInvalidHttpServletRequest_thenThrowException() {
        var request = createDefaultHttpServletRequest();
        request.addHeader("Authorization", malformedtoken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        assertThrows(InvalidAccessTokenException.class, () -> requestHelperService.getEmail());
    }

    @Test
    void whenEmailFound_thenReturnEmail() {
        var request = createDefaultHttpServletRequest();
        request.addHeader("Authorization", validToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        var response = requestHelperService.getEmail();

        assertEquals(DEFAULT_USER_EMAIL, response);
    }

}
