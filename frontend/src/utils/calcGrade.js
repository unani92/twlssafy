export function getGrade(score) {
  let grade = 0;
  if (score < 10) {
    grade = 1; // new
  } else if (score < 50) {
    grade = 2; // bronze
  } else if (score < 150) {
    grade = 3; // silver
  } else if (score < 300) {
    grade = 4; // gold
  } else if (score < 600) {
    grade = 5; // platinum
  } else {
    grade = 6; // diamond
  }
  return grade;
}
