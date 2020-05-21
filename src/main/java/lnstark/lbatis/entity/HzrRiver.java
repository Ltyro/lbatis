package lnstark.lbatis.entity;

public class HzrRiver {
	String id;
	String name;
	int grade;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return "HzrRiver [id=" + id + ", name=" + name + ", grade=" + grade + "]";
	}
	
}
