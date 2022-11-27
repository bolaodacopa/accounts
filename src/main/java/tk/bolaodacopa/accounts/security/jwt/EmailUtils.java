package tk.bolaodacopa.accounts.security.jwt;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String emailFrom;

	public void sendEmailForgotPassword(String recipientEmail, String token)  {
		try {
			MimeMessage message = mailSender.createMimeMessage();				
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(emailFrom, "Bolão da Copa");
			helper.setTo(recipientEmail);
			String resetPasswordLink =  "https://bolaodacopa.tk/resetpassword?token=" + token;
			String subject = "Bolão da Copa - Aqui está o link para redefinir sua senha";

			String content = "<p>Olá,</p>"
					+ "<p>Você solicitou a redefinição de sua senha.</p>"
					+ "<p>Clique no link abaixo para alterar sua senha:</p>"
					+ "<p><a href=\"" + resetPasswordLink + "\">Mudar minha senha</a></p>"
					+ "<br>"
					+ "<p>Ignore este e-mail se você lembra da sua senha "
					+ "ou não fez o pedido.</p>";

			helper.setSubject(subject);

			helper.setText(content, true);

			mailSender.send(message);
		} catch(Exception e) {
			new RuntimeException("Erro: Ao enviar email para redefinição de senha.");
		}
	}

}
