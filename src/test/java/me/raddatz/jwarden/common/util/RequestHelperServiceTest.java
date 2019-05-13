package me.raddatz.jwarden.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.raddatz.jwarden.common.error.UnauthorizedUserException;
import me.raddatz.jwarden.common.service.RequestHelperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RequestHelperService.class)
@ActiveProfiles("test")
class RequestHelperServiceTest {

    private final String validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTc2NDgwNTQsInVzZXJfbmFtZSI6ImtldmluQGZhbWlsaWUtcmFkZGF0ei5kZSIsImp0aSI6ImY2MmE5NjBhLWRjODMtNDBmMy04MjI3LWNmMzk1NjcwYWUzOSIsImNsaWVudF9pZCI6Imp3YXJkZW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.4eTwZxr_T-xzve6IEjOaEqvcxIPO6tPtwzJfkpTz5QE";
    private final String invalidToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTc2NDgwNTQsImp0aSI6ImY2MmE5NjBhLWRjODMtNDBmMy04MjI3LWNmMzk1NjcwYWUzOSIsImNsaWVudF9pZCI6Imp3YXJkZW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.l6IIcbrkGG2EmvQS1mk58DurMXVsmBNCtsYGUBDjPOs";

    @Autowired private RequestHelperService requestHelperService;
    @MockBean private ObjectMapper objectMapper;

    private MockHttpServletRequest getDefaultMockHttpServletRequest() {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", validToken);
        return request;
    }

    @Test
    void whenATPropertyNotFound_thenThrowException() {
        var request = getDefaultMockHttpServletRequest();
        request.removeHeader("Authorization");
        request.addHeader("Authorization", invalidToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThrows(UnauthorizedUserException.class, () -> requestHelperService.getUserName());
    }

    @Test
    void whenATPropertyFound_thenThrowException() {
        var request = getDefaultMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThrows(UnauthorizedUserException.class, () -> requestHelperService.getUserName());
    }

}