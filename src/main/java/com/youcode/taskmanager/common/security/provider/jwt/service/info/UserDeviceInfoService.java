package com.youcode.taskmanager.common.security.provider.jwt.service.info;

import com.youcode.taskmanager.common.security.dto.vm.UserDeviceInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserDeviceInfoService {

    public UserDeviceInfo getUserDeviceInfo(@NonNull HttpServletRequest request) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        checkNullAttribute(attributes);

        return UserDeviceInfo.builder()
                .ipAddress(attributes.getRequest().getRemoteAddr())
                .userAgent(attributes.getRequest().getHeader("User-Agent"))
                .build();
    }

    private void checkNullAttribute(ServletRequestAttributes attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Request attributes cannot be null");
        }
    }


    public void validateUserDeviceInfo(HttpServletRequest httpServletRequest, UserDeviceInfo userDeviceInfo) {
        UserDeviceInfo userDeviceInfo1 = getUserDeviceInfo(httpServletRequest);

        if (!userDeviceInfo1.equals(userDeviceInfo)) {
            throw new IllegalArgumentException("User device info is not valid!  try to login again");
        }
    }
}
