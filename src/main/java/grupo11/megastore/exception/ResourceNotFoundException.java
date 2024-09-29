package grupo11.megastore.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String resourceName;
	String field;
	String fieldName;
	Long fieldId;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s no encontrado con %s: %s", resourceName, field, fieldName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s no encontrado con %s: %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
}