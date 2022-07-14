package ipl.ei.mpoi.Objects;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PointMap implements Parcelable, Serializable {
    private String name;
    private ArrayList<PointOfInterest> points;

    public PointMap(String name){
            this.name = name;
            points = new ArrayList<PointOfInterest>();
            return;
    }

    public PointMap(String name, ArrayList<PointOfInterest> points){
        this.name = name;
        this.points = points;
        return;
    }

    public PointMap(File xmlFilePath, String fileName) throws ParserConfigurationException, IOException, SAXException {
        points = new ArrayList<PointOfInterest>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        //documentFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(xmlFilePath, fileName));
        document.getDocumentElement().normalize();

        Element map = (Element) document.getElementsByTagName("map").item(0);
        this.name = map.getAttribute("name");
        NodeList pointList = map.getChildNodes();
        for (int i = 0; i < pointList.getLength(); i++) {
            Node node = pointList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String name  = ((Element) node).getAttribute("name");
                String description  = ((Element) node).getAttribute("description");
                String category  = ((Element) node).getAttribute("category");

                Node position = node.getFirstChild();
                Double lat  = Double.parseDouble(((Element) position).getAttribute("lat"));
                Double lng  = Double.parseDouble(((Element) position).getAttribute("lng"));
                Double alt  = Double.parseDouble(((Element) position).getAttribute("lat"));
                PointOfInterest point = new PointOfInterest(name,lat,lng,alt,category,description);
                addPoint(point);
            }
        }
    }

    public PointMap(Document xml) throws ParserConfigurationException, IOException, SAXException {
        points = new ArrayList<PointOfInterest>();
        //xml.getDocumentElement().normalize();
        Element map = (Element) xml.getElementsByTagName("map").item(0);
        this.name = map.getAttribute("name");
        NodeList pointList = map.getChildNodes();
        for (int i = 0; i < pointList.getLength(); i++) {
            Node node = pointList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String name  = ((Element) node).getAttribute("name");
                String description  = ((Element) node).getAttribute("description");
                String category  = ((Element) node).getAttribute("category");
                NodeList nodeList = node.getChildNodes();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node position = nodeList.item(j);

                    if (position.getNodeType() == Node.ELEMENT_NODE) {
                        String latS = ((Element) position).getAttribute("lat");
                        Double lat = Double.parseDouble(latS);
                        Double lng = Double.parseDouble(((Element) position).getAttribute("lng"));
                        Double alt = Double.parseDouble(((Element) position).getAttribute("lat"));
                        PointOfInterest point = new PointOfInterest(name, lat, lng, alt, category, description);
                        addPoint(point);
                    }
                }

            }
        }
    }

    private PointMap(Parcel in){
        name = in.readString();
        points = new ArrayList<PointOfInterest>();
        in.readList(points,PointMap.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PointOfInterest> getPoints() {
        return points;
    }

    public void addPoint(PointOfInterest point){
        points.add(point);
    }

    public void removePoint(int position){
        points.remove(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(points);
    }

    public static final Parcelable.Creator<PointMap> CREATOR = new Parcelable.Creator<PointMap>() {
        public PointMap createFromParcel(Parcel in) {
            return new PointMap(in);
        }

        public PointMap[] newArray(int size) {
            return new PointMap[size];
        }
    };

    public void toXml(File xmlFilePath){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("map");
            root.setAttribute("name",getName());
            document.appendChild(root);

            for (PointOfInterest point : points) {
                Element elementPoint = document.createElement("point");
                elementPoint.setAttribute("name",point.getName());
                elementPoint.setAttribute("description",point.getDescription());
                elementPoint.setAttribute("category",point.getCategory());
                Element position = document.createElement("position");
                position.setAttribute("lat",Double.toString(point.getPosition().latitude));
                position.setAttribute("lng",Double.toString(point.getPosition().longitude));
                position.setAttribute("alt",Double.toString(point.getAltitude()));
                elementPoint.appendChild(position);
                root.appendChild(elementPoint);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            String filename = "PointMap-" + name + ".xml";
            StreamResult streamResult = new StreamResult(new File(xmlFilePath,filename));
            transformer.transform(domSource, streamResult);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public String toXml(){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("map");
            root.setAttribute("name",getName());
            document.appendChild(root);

            for (PointOfInterest point : points) {
                Element elementPoint = document.createElement("point");
                elementPoint.setAttribute("name",point.getName());
                elementPoint.setAttribute("description",point.getDescription());
                elementPoint.setAttribute("category",point.getCategory());
                Element position = document.createElement("position");
                position.setAttribute("lat",Double.toString(point.getPosition().latitude));
                position.setAttribute("lng",Double.toString(point.getPosition().longitude));
                position.setAttribute("alt",Double.toString(point.getAltitude()));
                elementPoint.appendChild(position);
                root.appendChild(elementPoint);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            DOMSource domSource = new DOMSource(document);

            transformer.transform(domSource, new StreamResult(writer));
            return writer.getBuffer().toString();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        return null;
    }
}
