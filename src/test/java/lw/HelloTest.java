package lw;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

public class HelloTest extends BaseTestCase {

	@Test
	public void testLogin() {
		try {
			ResultActions ra = mvc.perform(MockMvcRequestBuilders.get("/test/hello"));
			ra.andDo(MockMvcResultHandlers.print()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
