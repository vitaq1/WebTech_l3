package by.bsuir.wt2.server.command.implementation;

import by.bsuir.wt2.server.command.Command;
import by.bsuir.wt2.server.command.CommandException;
import by.bsuir.wt2.server.data.AuthType;
import by.bsuir.wt2.server.service.ServiceFactory;

public class DisconnectCommand implements Command {
    @Override
    public String execute(Object caller, String request) throws CommandException {
        ServiceFactory.getInstance().getAuthService().setAuthType(caller, AuthType.GUEST);
        return "Quit";
    }
}
