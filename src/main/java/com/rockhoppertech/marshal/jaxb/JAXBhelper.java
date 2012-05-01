package com.rockhoppertech.marshal.jaxb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rockhoppertech.marshal.domain.Composer;
import com.rockhoppertech.marshal.domain.Work;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
@Service
public class JAXBhelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		
		Composer composer = new Composer("Ludwig", "Beethoven");
		composer.setId(123L);

		Work work = new Work();
		work.setPremiereDate(new Date());
		work.setCompletionDate(new Date());
		work.setInstrumentation("kazoo, comb, wax paper");
		work.setTitle("Cacophony No. 2.718");
		composer.add(work);
		
		JAXBhelper helper = new JAXBhelper();
		helper.xmlToStdout(composer);
		
		helper.saveXML(composer, "composer.xml");
		Composer read = helper.readComposerXML("composer.xml");
		for(Work w: read.getWorks()) {
			System.out.println("Title: " + w.getTitle());
			System.out.println("Composer: " + w.getComposer());
		}
	}

	public JAXBhelper() {
	}

	public void xmlToStdout(Composer composer) {
		try {
			JAXBContext jaxbc = JAXBContext.newInstance(Composer.class);
			Marshaller marshaller = jaxbc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// StringWriter sw = new StringWriter();
			// marshaller.marshal(taskList, sw);
			// this.logger.debug(sw.toString());
			marshaller.marshal(composer, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}

	public void saveXML(Composer composer, String fileName) {
		try {
			JAXBContext jaxbc = JAXBContext.newInstance(Composer.class);
			Marshaller marshaller = jaxbc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter sw = new StringWriter();
			marshaller.marshal(composer, sw);
			this.logger.debug(sw.toString());
			OutputStream out = null;
			try {
				out = new FileOutputStream(fileName);
				marshaller.marshal(composer, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}

	public Composer readComposerXML(String fileName) {
		Composer composer = null;
		try {
			JAXBContext jaxbc = JAXBContext.newInstance(Composer.class);
			Unmarshaller marshaller = jaxbc.createUnmarshaller();
			InputStream file = null;
			try {
				file = new FileInputStream(fileName);
				composer = (Composer) marshaller.unmarshal(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				this.logger.error(e.getMessage());
			} finally {
				try {
					file.close();
				} catch (IOException e) {
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
		return composer;
	}

}
