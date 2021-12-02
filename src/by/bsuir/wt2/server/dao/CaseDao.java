package by.bsuir.wt2.server.dao;

import by.bsuir.wt2.server.data.Case;
import by.bsuir.wt2.server.service.ServiceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CaseDao {
    private static final CaseDao INSTANCE = new CaseDao();
    private static final String CASES_PATH = "src/resources/archive.xml";

    private final ReadWriteLock lock;
    private final Map<Integer, Case> cases;

    private CaseDao() {
        lock = new ReentrantReadWriteLock();
        cases = new HashMap<>();
        init();
    }

    private void init() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(CASES_PATH));
            doc.getDocumentElement().normalize();
            System.out.println(doc.getDoctype());
            NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Case _case = ServiceFactory.getInstance().getCaseService().createCase(node.getChildNodes());
                    cases.put(_case.getId(), _case);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ignored) {
        }
    }

    public static CaseDao getInstance() {
        return INSTANCE;
    }

    public boolean contains(int id) {
        return cases.containsKey(id);
    }

    public List<Case> getAll() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(cases.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void add(Case _case) {
        try {
            lock.writeLock().lock();
            _case.setId(cases.keySet().stream().max(Comparator.comparingInt(a -> a)).get() + 1);
            cases.put(_case.getId(), _case);
            update();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setById(int id, Case _case) {
        try {
            lock.writeLock().lock();
            _case.setId(id);
            cases.put(id, _case);
            update();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void update() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element rootEle = doc.createElement("cases");
            for(Case _case : getAll()) {
                Element caseEle = ServiceFactory.getInstance().getCaseService().createNode(doc, _case);
                rootEle.appendChild(caseEle);
            }

            doc.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(CASES_PATH)));

            } catch (IOException | TransformerException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}