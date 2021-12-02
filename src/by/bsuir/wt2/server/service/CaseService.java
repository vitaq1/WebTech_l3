package by.bsuir.wt2.server.service;


import by.bsuir.wt2.server.dao.DaoFactory;
import by.bsuir.wt2.server.data.Case;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class CaseService {
    private static final CaseService INSTANCE = new CaseService();

    private CaseService() {
    }

    public static CaseService getInstance() {
        return INSTANCE;
    }

    public Case createCase(NodeList nodes) {
        int id = 0;
        String first = "";
        String last = "";

        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String text = nodes.item(i).getTextContent();
                String nodeElem = nodes.item(i).getNodeName();
                System.out.println(nodeElem);
                switch (nodeElem) {
                    case "id":
                        id = Integer.parseInt(text);
                        break;
                    case "firstName":
                        first = text;
                        break;
                    case "lastName":
                        last = text;
                        break;
                    default:
                        throw new IllegalArgumentException("No such case exists");
                }
            }
        }

        return new Case(id, first, last);
    }

    public Element createNode(Document doc, Case _case) {
        Element e = doc.createElement("case");
        Element id = doc.createElement("id");
        Element first = doc.createElement("firstName");
        Element last = doc.createElement("lastName");
        id.appendChild(doc.createTextNode(String.valueOf(_case.getId())));
        first.appendChild(doc.createTextNode(_case.getFirstName()));
        last.appendChild(doc.createTextNode(_case.getLastName()));
        e.appendChild(id);
        e.appendChild(first);
        e.appendChild(last);
        return e;
    }

    public List<Case> getAll() {
        return DaoFactory.getInstance().getCaseDao().getAll();
    }

    public boolean containsCase(int id) {
        return DaoFactory.getInstance().getCaseDao().contains(id);
    }

    public void editCase(int id, String firstName, String lastName) {
        DaoFactory.getInstance().getCaseDao().setById(id, new Case(0, firstName, lastName));
    }

    public void addCase(String firstName, String lastName) {
        DaoFactory.getInstance().getCaseDao().add(new Case(0, firstName, lastName));
    }
}