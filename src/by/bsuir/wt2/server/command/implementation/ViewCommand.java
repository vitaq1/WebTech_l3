package by.bsuir.wt2.server.command.implementation;

import by.bsuir.wt2.server.command.Command;
import by.bsuir.wt2.server.command.CommandException;
import by.bsuir.wt2.server.data.AuthType;

import by.bsuir.wt2.server.data.Case;
import by.bsuir.wt2.server.service.ServiceFactory;

import java.util.List;

public class ViewCommand implements Command {
    @Override
    public String execute(Object caller, String request) throws CommandException {
        if (ServiceFactory.getInstance().getAuthService().getAuthType(caller) == AuthType.GUEST)
            return "Should be authenticated";

        List<Case> cases = ServiceFactory.getInstance().getCaseService().getAll();
        return toOutput(cases);
    }

    private static String toOutput(List<Case> cases) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("[\n");
        for (Case _case : cases) {
            resultBuilder.append("\t").append(_case.toString()).append("\n");
        }
        resultBuilder.append("]");
        return resultBuilder.toString();
    }
}
