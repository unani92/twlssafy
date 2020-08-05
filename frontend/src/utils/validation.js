export function validateEmail(value) {
	const re = /\S+@\S+\.\S+/;
	return re.test(value);
}

export function validatePassword(value) {
	const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
	return re.test(value);
}

export function validateMarkdown(value) {
	const re1 = /[^a-zA-Zㄱ-힣0-9<>.]/g;
	value.replace(re1,' ');
	const re2 = /!\[(.*?)\]/;
	value = value.replace(re2,'');
	const re3 = /\((.*?)\)/;
	return value.replace(re3,'');
}