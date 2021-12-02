package by.bsuir.wt2.server.service;

public class ServiceFactory {
    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public AuthService getAuthService() {
        return AuthService.getInstance();
    }

    public CaseService getCaseService() {
        return CaseService.getInstance();
    }
}