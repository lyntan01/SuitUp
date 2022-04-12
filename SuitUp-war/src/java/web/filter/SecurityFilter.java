/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.StaffEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author lyntan
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/SuitUp-war";

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                StaffEntity currentStaffEntity = (StaffEntity) httpSession.getAttribute("currentStaffEntity");
                if (checkAccessRight(requestServletPath, currentStaffEntity.getAccessRightEnum())) {
                    chain.doFilter(request, response);
                } else {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    
    private Boolean checkAccessRight(String path, AccessRightEnum accessRight)
    {   
        if(accessRight.equals(AccessRightEnum.TAILOR))
        {            
            if(path.equals("/staffProfile.xhtml") ||
                path.equals("/customerManagement.xhtml"))
            {
                return true;
            }
            else
            {
                return false;
            }
        } else if (accessRight.equals(AccessRightEnum.CASHIER)) {
            if (path.equals("/staffProfile.xhtml")
                    || path.equals("/orderManagement.xhtml")
                    || path.equals("/appointmentManagement.xhtml")) {
                return true;
            } else {
                return false;
            }
        } else if (accessRight.equals(AccessRightEnum.MANAGER)) {
            if (path.equals("/staffProfile.xhtml")
                    || path.equals("/products/standardProductManagement.xhtml")
                    || path.equals("/products/customizationManagement.xhtml")
                    || path.equals("/appointmentManagement.xhtml")
                    || path.equals("/customerManagement.xhtml")
                    || path.equals("/orderManagement.xhtml")
                    || path.equals("/promotionManagement.xhtml")
                    || path.equals("/staffManagement.xhtml")
                    || path.equals("/storeManagement.xhtml")
                    || path.equals("/supportTicketManagement.xhtml")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/accessRightError.xhtml")
                || path.startsWith("/javax.faces.resource")
                || path.startsWith("/uploadedFiles")) {
            return true;
        } else {
            return false;
        }
    }

}
