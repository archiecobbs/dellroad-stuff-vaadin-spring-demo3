
package com.example;

import javax.xml.stream.XMLStreamWriter;

import org.dellroad.stuff.xml.EmptyTagXMLStreamWriter;
import org.dellroad.stuff.xml.IndentXMLStreamWriter;
import org.springframework.oxm.jibx.JibxMarshaller;

/**
 * Workaround for <a href="http://jira.codehaus.org/browse/JIBX-492">JIBX-492</a>.
 */
public class WorkaroundJibxMarshaller extends JibxMarshaller {

    @Override
    protected void marshalXmlStreamWriter(Object graph, XMLStreamWriter writer) {
        super.marshalXmlStreamWriter(graph, new EmptyTagXMLStreamWriter(new IndentXMLStreamWriter(writer)));
    }
}

