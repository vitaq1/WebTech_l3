package by.bsuir.wt2.server.command;


public interface Command {
    String execute(Object caller, String request) throws CommandException;
}
