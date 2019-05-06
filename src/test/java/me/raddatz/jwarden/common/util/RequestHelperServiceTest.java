package me.raddatz.jwarden.common.util;

import me.raddatz.jwarden.common.service.RequestHelperService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(RequestHelperService.class)
@ActiveProfiles("test")
class RequestHelperServiceTest {

    @Autowired private RequestHelperService requestHelperService;

    private static ServletRequestAttributes getDefaultServletRequestAttributes() {
        var request = new MockHttpServletRequest();
        request.addHeader("Authentication", "basic:auth");
        return new ServletRequestAttributes(request);
    }

    @Test
    void getAuthenticationHeaderWithServletRequestAttributes() {
        RequestContextHolder.setRequestAttributes(getDefaultServletRequestAttributes());
        assertEquals("basic:auth", requestHelperService.getAuthenticationHeader());
    }

    @Test
    void getAuthenticationHeaderWithoutServletRequestAttributes() {
        RequestContextHolder.setRequestAttributes(null);
        assertEquals("", requestHelperService.getAuthenticationHeader());
    }
}