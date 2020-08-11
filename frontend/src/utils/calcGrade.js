export function getGrade(score) {
  let grade = 0;
  if (score <= 0) {
    grade = 1;
    return grade;
  } else if (score <= 50) {
    grade++;
    return grade;
  } else if (score <= 150) {
    grade++;
    return grade;
  } else if (score <= 300) {
    grade++;
    return grade;
  } else if (score <= 600) {
    grade++;
    return grade;
  }
  grade++;
  return grade;
}
