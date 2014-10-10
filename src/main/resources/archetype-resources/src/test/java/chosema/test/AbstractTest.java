package chosema.test;

import javax.servlet.http.HttpServletRequest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public abstract class AbstractTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("spring.profiles.active", "test");

		final HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
		Mockito.when(requestMock.getRemoteUser()).thenReturn("test.user");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestMock));
	}

	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(final Description description) {
			System.out.println("Starting test: " + description.getMethodName());
		}

		@Override
		protected void finished(final Description description) {
			System.out.println("Finished test: " + description.getMethodName() + "\n");
		}
	};

}
