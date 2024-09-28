package grupo11.megastore.config;

public class AppConstants {
    
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	public static final String[] PUBLIC_URLS = { "/auth/login", "/auth/register" };
	public static final String[] USER_URLS = { "/public/**" };
	public static final String[] ADMIN_URLS = { "/admin/**" };

}
 