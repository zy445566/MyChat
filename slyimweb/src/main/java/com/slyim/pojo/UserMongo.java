package com.slyim.pojo;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.slyim.common.ReadWriteBase;
import com.slyim.common.ReadWriteBase.FileType;

public class UserMongo{
	private String id;
	private String name;
	private String avatar;
	private String phone;
	private String password;
	//个性签名
	private String sign=null;
	//擅长
	private List<String> strong;
	private int sex=0;
	//租金
	private float rent=0;
	//地点定位
	private Localtion localtion;
	//上次资料修改时间，如是本人可为上线时间
	private long modifyTime;
	//VIP得分1，300，600，1200，2400，4800, 9600, 19200,38400,76800
	private int vipScore=0;
	//VIP到期时间
	private long vipDeadLine;
	//VIP刷新时间
	private long vipRefreshTime;
	
	public UserMongo()
	{
		
	}
	
	public UserMongo(String id, String name, String password)
	{
		this.setId(id);
		this.setName(name);
		this.setPassword(password);
	}
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public int getSex()
	{
		return sex;
	}
	public void setSex(int sex)
	{
		this.sex = sex;
	}
	public float getRent()
	{
		return rent;
	}
	public void setRent(float rent)
	{
		this.rent = rent;
	}
	public String getSign()
	{
		return sign;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public List<String> getStrong()
	{
		return strong;
	}

	public void setStrong(List<String> strong)
	{
		this.strong = strong;
	}

	public Localtion getLocaltion()
	{
		return localtion;
	}

	public void setLocaltion(Localtion localtion)
	{
		this.localtion = localtion;
	}
	
	public boolean isEmptyPhone()
	{
		if (this.phone==null) {
			return true;
		}
		Pattern pattern = Pattern.compile("^1\\d{10}$"); 
		return !pattern.matcher(this.phone).matches(); 
	}
	
	public boolean isEmptyPassword()
	{
		if (this.password==null) {
			return true;
		}
		Pattern pattern = Pattern.compile("^\\d{6}$"); 
		return !pattern.matcher(this.password).matches(); 
	}
	
	public boolean isEmptySex()
	{
		if (this.sex!=1 && this.sex!=2) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEmptyStrong()
	{
		if (this.strong.isEmpty()) {
			return true;
		} else {
			for (String singleStrong :this.strong) {
				if (singleStrong==null || singleStrong.equals("")) {
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean isEmptyRent()
	{
		if (this.rent<0) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isEmptyAvatar(){
		if (this.avatar==null || this.avatar.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEmptyName(){
		if (this.name==null || this.name.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	public String toString()
	{
		return "Phone:"+this.phone+"password:"+this.password+"sex:"+this.sex+"rent:"+this.rent+"localtion:"+this.localtion;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getVipScore() {
		return vipScore;
	}

	public void setVipScore(int vipScore) {
		this.vipScore = vipScore;
	}

	public long getVipDeadLine() {
		return vipDeadLine;
	}

	public void setVipDeadLine(long vipDeadLine) {
		this.vipDeadLine = vipDeadLine;
	}

	public long getVipRefreshTime() {
		return vipRefreshTime;
	}

	public void setVipRefreshTime(long vipRefreshTime) {
		this.vipRefreshTime = vipRefreshTime;
	}
	
	public boolean converAvatar(HttpServletRequest request, FileType fileType) {
		Pattern pattern = Pattern.compile("^data:.*?"); 
		if (pattern.matcher(this.avatar).matches()) {
			try {
				this.avatar=ReadWriteBase.writeBytesByBase64(request, fileType, this.avatar);
				return true;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
		
	}
	


}
