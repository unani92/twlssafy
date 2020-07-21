export function validateEmail(value) {
	const re = /\S+@\S+\.\S+/;
	return re.test(value);
}

export function validatePassword(value) {
	const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
	return re.test(value);
}