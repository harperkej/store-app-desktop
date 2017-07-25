package thesis.buyproducts.restcall.model;

import java.util.List;

public class ValidationErrorDto {

	private List<FieldErrorDto> listOfErrors;

	public List<FieldErrorDto> getListOfErrors() {
		return listOfErrors;
	}

	public void setListOfErrors(List<FieldErrorDto> listOfErrors) {
		this.listOfErrors = listOfErrors;
	}

}
