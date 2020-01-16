package com.company.watches.web;

import com.company.watches.dao.DAOContainer;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.utils.WebUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "rootServlet", urlPatterns = {"/api/*"})
public class RootServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RootServlet.class.getName());

    private Map<String, Controller> registeredControllers = new HashMap<>();

    @Override
    public void init() {
        final ManagersContainer managers = ManagersContainer.getInstance(DAOContainer.getInstance());

        registerController("country/", new CountryController(managers));
        registerController("customer/", new CustomerController(managers));
        registerController("vendor/", new VendorController(managers));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        ResponseWrapper responseWrapper = dispatchRequest(toRequestWrapper(request));
        sendResponse(response, responseWrapper);
    }

    private ResponseWrapper dispatchRequest(RequestWrapper requestWrapper) {
        for (Map.Entry<String, Controller> entry : registeredControllers.entrySet()) {
            if (requestWrapper.path.startsWith(entry.getKey())) {
                return entry.getValue().process(requestWrapper);
            }
        }

        return ResponseWrapper.NotFound();
    }

    private void registerController(String route, Controller controller) {
        registeredControllers.put(route, controller);
    }

    private RequestWrapper toRequestWrapper(HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final String payload = WebUtils.getPayload(request);
        final String path = request.getPathInfo() == null ? "" : request.getPathInfo().substring(1);
        final RequestWrapper.RequestMethod requestMethod = RequestWrapper.RequestMethod.valueOf(request.getMethod());

        return new RequestWrapper(requestMethod, path, parameterMap, payload);
    }

    private void sendResponse(HttpServletResponse response, ResponseWrapper responseWrapper) {
        response.setCharacterEncoding("utf-8");
        response.setStatus(responseWrapper.statusCode);
        response.setContentType(responseWrapper.contentType.typeName);
        try {
            response.getWriter().print(responseWrapper.data);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "send response error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
