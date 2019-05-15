package me.raddatz.yapam.common.service;

import me.raddatz.yapam.common.error.UnauthorizedUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RequestHelperService.class)
@ActiveProfiles("test")
class RequestHelperServiceTest {

    private final String validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTc2NDgwNTQsInVzZXJfbmFtZSI6InVzZXJAZW1haWwuY29tIiwianRpIjoiZjYyYTk2MGEtZGM4My00MGYzLTgyMjctY2YzOTU2NzBhZTM5IiwiY2xpZW50X2lkIjoiandhcmRlbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.ef9i1BqVt7EACKOUk9e-yLAsjqbHA4ZjvAgmxHBgA7o";
    private final String invalidToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTc2NDgwNTQsImp0aSI6ImY2MmE5NjBhLWRjODMtNDBmMy04MjI3LWNmMzk1NjcwYWUzOSIsImNsaWVudF9pZCI6Imp3YXJkZW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.l6IIcbrkGG2EmvQS1mk58DurMXVsmBNCtsYGUBDjPOs";
    private final String malformedToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAifQ==.EoEbyNXjh5_7qHl7I78l32P9eQpr6HuZ_R1FaZX8ED4";

    @Autowired private RequestHelperService requestHelperService;

    private MockHttpServletRequest getDefaultMockHttpServletRequest() {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", validToken);
        return request;
    }

    @Test
    void whenInvalidHttpServletRequest_thenThrowException() {
        RequestContextHolder.setRequestAttributes(null);
        assertThrows(Exception.class, () -> requestHelperService.getUserName());
    }

    @Test
    void whenATMalformed_thenThrowException() {
        var request = getDefaultMockHttpServletRequest();
        request.removeHeader("Authorization");
        request.addHeader("Authorization", malformedToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThrows(UnauthorizedUserException.class, () -> requestHelperService.getUserName());
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

        var response = requestHelperService.getUserName();

        assertEquals("user@email.com", response);
    }

}