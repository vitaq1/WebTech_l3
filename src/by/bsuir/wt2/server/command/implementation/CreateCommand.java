package by.bsuir.wt2.server.command.implementation;

import by.bsuir.wt2.server.command.Command;
import by.bsuir.wt2.server.command.CommandException;
import by.bsuir.wt2.server.data.AuthType;
import by.bsuir.wt2.server.service.ServiceFactory;

public class CreateCommand implements Command {
    @Override
    public String execute(Object caller, String request) throws CommandException {
        String[] arguments = request.split(" ");
        if (arguments.length != 3) throw new CommandException("CREATE invalid syntax");

        if (ServiceFactory.getInstance().getAuthService().getAuthType(caller) != AuthType.ADMIN)
            return "Should be ADMIN";

        ServiceFactory.getInstance().getCaseService().addCase(arguments[1], arguments[2]);
        return "Success";
    }
}
