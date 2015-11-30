public class Player {

private int id;
//@KmeanField
private String name;

private int age;

/* 得分 */
@KmeanField
private double goal;

/* 助攻 */
//@KmeanField
private double assists;

/* 篮板 */
//@KmeanField
private double backboard;

/* 抢断 */
//@KmeanField
private double steals;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getAge() {
	return age;
}

public void setAge(int age) {
	this.age = age;
}

public double getGoal() {
	return goal;
}

public void setGoal(double goal) {
	this.goal = goal;
}

public double getAssists() {
	return assists;
}

public void setAssists(double assists) {
	this.assists = assists;
}

public double getBackboard() {
	return backboard;
}

public void setBackboard(double backboard) {
	this.backboard = backboard;
}

public double getSteals() {
	return steals;
}

public void setSteals(double steals) {
	this.steals = steals;
}

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

