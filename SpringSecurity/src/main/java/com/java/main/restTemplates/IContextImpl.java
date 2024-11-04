package com.java.main.restTemplates;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.IWebSession;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

public class IContextImpl implements IWebContext {

    private final Map<String, Object> variables;
    private final IWebExchange webExchange;
    private final Locale locale;

    public IContextImpl(Map<String, Object> variables) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        this.webExchange = new IWebExchangeImpl(request, response);
        this.locale = request.getLocale();
        this.variables = variables;
    }
    @Override
    public Object getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public IWebExchange getExchange() {
        return webExchange;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    // Helper class for IWebExchange implementation
    private static class IWebExchangeImpl implements IWebExchange {

        private final HttpServletRequest request;
        private final HttpServletResponse response;

        public IWebExchangeImpl(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }


        @Override
        public IWebRequest getRequest() {
            return new IWebRequestImpl(request);
        }


        public class IWebRequestImpl implements IWebRequest {

            private final HttpServletRequest request;

            public IWebRequestImpl(HttpServletRequest request) {
                this.request = request;
            }

            @Override
            public String getMethod() {
                return request.getMethod();
            }

            @Override
            public String getScheme() {
                return request.getScheme();
            }

            @Override
            public String getServerName() {
                return request.getServerName();
            }

            @Override
            public Integer getServerPort() {
                return request.getServerPort();
            }

            @Override
            public String getApplicationPath() {
                return request.getContextPath();
            }

            @Override
            public String getPathWithinApplication() {
                return request.getRequestURI().substring(request.getContextPath().length());
            }

            @Override
            public String getQueryString() {
                return request.getQueryString();
            }

            @Override
            public boolean containsHeader(String name) {
                return request.getHeader(name) != null;
            }

            @Override
            public int getHeaderCount() {
                return Collections.list(request.getHeaderNames()).size();
            }

            @Override
            public Set<String> getAllHeaderNames() {
                return Collections.list(request.getHeaderNames()).stream().collect(Collectors.toSet());
            }

            @Override
            public Map<String, String[]> getHeaderMap() {
                return Collections.list(request.getHeaderNames()).stream()
                        .collect(Collectors.toMap(
                                name -> name,
                                name -> Collections.list(request.getHeaders(name)).toArray(String[]::new)
                        ));
            }

            @Override
            public String[] getHeaderValues(String name) {
                return Collections.list(request.getHeaders(name)).toArray(String[]::new);
            }

            @Override
            public boolean containsParameter(String name) {
                return request.getParameter(name) != null;
            }

            @Override
            public int getParameterCount() {
                return request.getParameterMap().size();
            }

            @Override
            public Set<String> getAllParameterNames() {
                return request.getParameterMap().keySet();
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return request.getParameterMap();
            }

            @Override
            public String[] getParameterValues(String name) {
                return request.getParameterValues(name);
            }

            @Override
            public boolean containsCookie(String name) {
                return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .anyMatch(cookie -> cookie.getName().equals(name));
            }

            @Override
            public int getCookieCount() {
                return Optional.ofNullable(request.getCookies()).map(cookies -> cookies.length).orElse(0);
            }

            @Override
            public Set<String> getAllCookieNames() {
                return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .map(Cookie::getName)
                        .collect(Collectors.toSet());
            }

            @Override
            public Map<String, String[]> getCookieMap() {
                return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .collect(Collectors.toMap(Cookie::getName, cookie -> new String[]{cookie.getValue()}));
            }

            @Override
            public String[] getCookieValues(String name) {
                return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .filter(cookie -> cookie.getName().equals(name))
                        .map(Cookie::getValue)
                        .toArray(String[]::new);
            }

            public IWebSession getSession() {
                return new IWebSessionImpl(request.getSession());
            }

            public IWebApplication getApplication() {
                return new IWebApplicationImpl(request.getServletContext());
            }
        }



        @Override
        public IWebSession getSession() {
            return new IWebSessionImpl(request.getSession()); // Custom session implementation.
        }

        @Override
        public IWebApplication getApplication() {
            return new IWebApplicationImpl(request.getServletContext());
        }


        public class IWebApplicationImpl implements IWebApplication {

            private final ServletContext servletContext;
            private final Map<String, Object> attributes = new HashMap<>();

            public IWebApplicationImpl(ServletContext servletContext) {
                this.servletContext = servletContext;
            }

            @Override
            public boolean containsAttribute(String name) {
                return attributes.containsKey(name);
            }

            @Override
            public int getAttributeCount() {
                return attributes.size();
            }

            @Override
            public Set<String> getAllAttributeNames() {
                return attributes.keySet();
            }

            @Override
            public Map<String, Object> getAttributeMap() {
                return Collections.unmodifiableMap(attributes);
            }

            @Override
            public Object getAttributeValue(String name) {
                return attributes.get(name);
            }

            @Override
            public void setAttributeValue(String name, Object value) {
                attributes.put(name, value);
            }

            @Override
            public void removeAttribute(String name) {
                attributes.remove(name);
            }

            @Override
            public boolean resourceExists(String path) {
                // Check if a resource exists in the web application using the servlet context.
                return servletContext.getResourceAsStream(path) != null;
            }

            @Override
            public InputStream getResourceAsStream(String path) {
                // Get the resource as an input stream if available.
                return servletContext.getResourceAsStream(path);
            }
        }



        @Override
        public Principal getPrincipal() {
            return request.getUserPrincipal(); // Return the authenticated principal, if any.
        }

        @Override
        public Locale getLocale() {
            return request.getLocale(); // Retrieve locale from request.
        }

        @Override
        public String getContentType() {
            return request.getContentType() != null ? request.getContentType() : "";
        }

        @Override
        public String getCharacterEncoding() {
            return request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8";
        }

        @Override
        public boolean containsAttribute(String name) {
            return request.getAttribute(name) != null;
        }

        @Override
        public int getAttributeCount() {
            return request.getAttributeNames().asIterator().hasNext() ? 1 : 0;
        }

        @Override
        public Set<String> getAllAttributeNames() {
            Set<String> attributeNames = new HashSet<>();
            request.getAttributeNames().asIterator().forEachRemaining(attributeNames::add);
            return attributeNames;
        }

        @Override
        public Map<String, Object> getAttributeMap() {
            Map<String, Object> attributeMap = new HashMap<>();
            request.getAttributeNames().asIterator()
                    .forEachRemaining(name -> attributeMap.put(name, request.getAttribute(name)));
            return attributeMap;
        }

        @Override
        public Object getAttributeValue(String name) {
            return request.getAttribute(name);
        }

        @Override
        public void setAttributeValue(String name, Object value) {
            request.setAttribute(name, value);
        }

        @Override
        public void removeAttribute(String name) {
            request.removeAttribute(name);
        }

        @Override
        public String transformURL(String url) {
            return response.encodeURL(url); // Proper URL encoding via response.
        }

        // Helper class for session handling.
        private static class IWebSessionImpl implements IWebSession {

            private final HttpSession session;

            public IWebSessionImpl(HttpSession session) {
                this.session = session;
            }

            @Override
            public boolean exists() {
                // Check if the session exists.
                return session != null;
            }

            @Override
            public boolean containsAttribute(String name) {
                // Check if the session contains a specific attribute.
                return session.getAttribute(name) != null;
            }

            @Override
            public int getAttributeCount() {
                // Count the number of attributes in the session.
                return session.getAttributeNames().asIterator().hasNext() ? 1 : 0;
            }

            @Override
            public Set<String> getAllAttributeNames() {
                Set<String> attributeNames = new HashSet<>();
                session.getAttributeNames().asIterator().forEachRemaining(attributeNames::add);
                return attributeNames;
            }

            @Override
            public Map<String, Object> getAttributeMap() {
                // Create a map of all session attributes.
                Map<String, Object> attributeMap = new HashMap<>();
                session.getAttributeNames().asIterator()
                        .forEachRemaining(name -> attributeMap.put(name, session.getAttribute(name)));
                return attributeMap;
            }

            @Override
            public Object getAttributeValue(String name) {
                // Retrieve the value of a specific attribute.
                return session.getAttribute(name);
            }

            @Override
            public void setAttributeValue(String name, Object value) {
                // Set or update the value of a session attribute.
                session.setAttribute(name, value);
            }

            @Override
            public void removeAttribute(String name) {
                session.removeAttribute(name);
            }
        }
    }
}