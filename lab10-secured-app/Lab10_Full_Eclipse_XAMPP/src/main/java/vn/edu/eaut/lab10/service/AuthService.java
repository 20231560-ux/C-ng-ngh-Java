package vn.edu.eaut.lab10.service;
import vn.edu.eaut.lab10.model.User; import vn.edu.eaut.lab10.repository.UserRepository;
public class AuthService { private final UserRepository r=new UserRepository(); public User login(String e,String p){User u=r.findByEmail(e);return u!=null&&u.isActive()&&u.getPassword().equals(p)?u:null;} }