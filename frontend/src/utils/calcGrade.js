export function getGrade(score) {
  let grade = 0;
  if (score <= 0) {
    grade = 1;
  } else if (score <= 50) {
    grade = 2;
  } else if (score <= 150) {
    grade = 3;
  } else if (score <= 300) {
    grade = 4;
  } else if (score <= 600) {
    grade = 5;
  } else {
    grade = 6;
  }
  return grade;
}
