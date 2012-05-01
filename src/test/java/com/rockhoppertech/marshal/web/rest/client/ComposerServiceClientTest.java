package com.rockhoppertech.marshal.web.rest.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.rockhoppertech.marshal.domain.Composer;
import com.rockhoppertech.marshal.domain.Work;
import com.rockhoppertech.marshal.jaxb.ComposerList;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = { "classpath:spring-testcontext.xml" })
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"classpath:spring-testcontext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ComposerServiceClientTest {

	private Logger log = LoggerFactory
			.getLogger(ComposerServiceClientTest.class);

	private ComposerServiceClient client;

	@Before
	public void setUp() {
		this.client = new ComposerServiceClient();
	}

	/**
	 * Pass condition: A composer with a known name is found.
	 */
	@Test
	public void shouldFindByFamilyName() {
		String familyName = "Bach";

		ComposerList cl = client.findByFamilyName(familyName);
		assertThat("Composer list is not null", cl, is(notNullValue()));
		log.info("Size of composer list is " + cl.getComposers().size());

		Composer c = cl.getComposers().get(0);

		assertThat("Composer is not null", c, is(notNullValue()));
		assertThat("Composer family name is not null", c.getFamilyName(),
				is(notNullValue()));
		assertThat("Composer family name is equal to the given family name",
				c.getFamilyName(), is(equalTo(familyName)));

		log.info(String.format("Get composer by name <%s>", c.getFamilyName()));
	}

	@Test
	public void shouldFindAll() {
		ComposerList cl = client.finalAll();
		assertThat("ComposerList is not null", cl, is(notNullValue()));

		assertThat("ComposerList has at least one composer", cl.getComposers()
				.isEmpty(), is(equalTo(Boolean.FALSE)));
	}

	@Test
	public void shouldCreate() {
		String familyName = "L";
		Composer data = new Composer("F", "M", familyName);

		Composer composer = client.create(data);
		assertThat("Composer is not null", composer, is(notNullValue()));

		log.debug("Posted this composer: " + composer);

		assertThat("Composer is not null", composer, is(notNullValue()));
		assertThat("Composer family name is not null",
				composer.getFamilyName(), is(notNullValue()));
		assertThat("Composer name is equal to the given name",
				composer.getFamilyName(), is(equalTo(familyName)));
		log.debug("Saved Composer with family name " + composer.getFamilyName());

		ComposerList cl = client.findByFamilyName(familyName);
		assertThat("Composer list is not null", cl, is(notNullValue()));
		assertThat("Composers has at least one composer", cl.getComposers()
				.isEmpty(), is(equalTo(Boolean.FALSE)));

		log.debug("ComposerList size " + cl.getComposers().size());

		Composer c = cl.getComposers().get(0);
		assertThat("Composer is not null", c, is(notNullValue()));
		assertThat("Composer name is equal to the given name",
				c.getFamilyName(), is(equalTo(familyName)));

		// client.delete(composer);

	}

	/**
	 * Pass condition:
	 */
	@Test
	public void shouldAddWork() {
		String familyName = "Bach";

		ComposerList cl = client.findByFamilyName(familyName);
		assertThat("Composer list is not null", cl, is(notNullValue()));
		log.info("Size of composer list is " + cl.getComposers().size());

		Composer c = cl.getComposers().get(0);

		assertThat("Composer is not null", c, is(notNullValue()));
		assertThat("Composer family name is not null", c.getFamilyName(),
				is(notNullValue()));
		assertThat("Composer family name is equal to the given family name",
				c.getFamilyName(), is(equalTo(familyName)));

		log.info(String.format("Get composer by name <%s>", c.getFamilyName()));

		Work work = new Work();
		work.setTitle("Thingy");
		c.add(work);
		// this.client.create(work);
		this.client.update(c);
		log.debug("work id is " + work.getId());
	}
}
