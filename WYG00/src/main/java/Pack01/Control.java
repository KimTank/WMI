package Pack01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class Control {
	Dao dao = new Dao();
	UserInfor ui = new UserInfor();

	// �α��� ���������� �α����ϴ� �۵��ϰ� �����
	@RequestMapping("/t2loginInfor")
	public String loginAct(HttpServletRequest request, @RequestParam(value = "id") String id,
			@RequestParam(value = "pass") String pass, Model model) {
		// Ȯ�ο�
		System.out.println(id + " " + pass);
		try {
			System.out.println(Class.forName("oracle.jdbc.driver.OracleDriver"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		// ������
		UserInfor ui = new UserInfor(id, pass);
		// ���� �� int�� �ҷ�����
		String login = "";
		login = dao.login(ui);// ���������ϸ� 1 �����ϸ� 0�� �������� id���� �Ѿ�������Ͽ���
		System.out.println(login);
		// -----------------------
		if (login != "") {// �α��ο� ����������
			System.out.println("�α��μ���");
			// ������ ��Ʈ�ѷ����� ������
			HttpSession session = request.getSession();
			session.setAttribute("login", login);
			// �����ϸ� main���� ���� �����س��Ҵ�
			return "main";
		} else {// �α��ο� ����������
			System.out.println("�α��ν���");
			// �α��ν����Ͻ�
			HttpSession session = request.getSession();
			session.setAttribute("login", null);
			return "/user/loginFail";
		}
	}

	// �α��� ȭ�鿡�� Ajax����Ͽ� ������ �� �ֵ��� �ϴ� �Լ�
	@RequestMapping(value = "checkId.jy", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody void idCheck(UserInfor user, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		System.out.println("���ɾ���");
		Dao dao = new Dao();
		JSONObject json = new JSONObject();// ���̽� ��ü����
		json.put("idCheck", dao.idCheck(user));// ���̽� ���� ����
		String s = json.toString();
		System.out.println(s);

		PrintWriter writer = response.getWriter();// �����͸� �����
		writer.write(s);
		writer.flush();// ��ü�� ����������
		writer.close();
	}

	// �α׾ƿ� ó������
	@RequestMapping("/t1logout")
	public String logoutAct(HttpServletRequest request) {
		dao.logout(request);
		return "main";
	}

	// ȸ������ ó������
	@RequestMapping("/t2signup")
	public String signupAct(@RequestParam(value = "id") String id, @RequestParam(value = "pass") String pass,
			@RequestParam(value = "gender") String gender, @RequestParam(value = "name") String name,
			@RequestParam(value = "birth") String birth, @RequestParam(value = "cell") String cell,
			@RequestParam(value = "email") String email) {
		// Ȯ�ο�
		System.out.println(id + " " + pass + " " + gender + " " + name + " " + birth + " " + cell + " " + email);
		try {
			System.out.println(Class.forName("oracle.jdbc.driver.OracleDriver"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		dao.signup(new UserInfor(id, pass, gender, name, birth, cell, email));
		return "/user/login";
	}
}
