package edu.ucr.cs242.common.util;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 *
 * private static final JAXBContext jaxbContext = JaxbUtils.createJAXBContext(Page.class);
 *
 */
public class JaxbUtils {

  private JAXBContext jaxbContext;

  private JaxbUtils(){
  }

  public static JAXBContext createJAXBContext(Class objectFactory) {
    try {
      return JAXBContext.newInstance(objectFactory);
    } catch (JAXBException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private JaxbUtils(JAXBContext jaxbContext){
    this.jaxbContext = jaxbContext;
  }

  public static JaxbUtils getInstance(JAXBContext jaxbContext) {
    return new JaxbUtils(jaxbContext);
  }

  public <T> T xmlToBean(InputStream xmlStream) {
    try {

      return (T) jaxbContext.createUnmarshaller().unmarshal(xmlStream);

    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
  
  public <T> T xmlToBean(StringReader xmlStream1) {
	    try {

	      return (T) jaxbContext.createUnmarshaller().unmarshal(xmlStream1);

	    } catch (JAXBException e) {
	      throw new RuntimeException(e);
	    }
	  }

  public String beanToXml(Object bean) {
    try {
      StringWriter sw = new StringWriter();
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(bean, sw);
      return sw.toString();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
