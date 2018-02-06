/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;


public interface EmployeeControllerRemote {

    public EmployeeEntity createNewEmployee(EmployeeEntity employeeEntity) throws EmployeeExistException, GeneralException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public void updateEmployee(EmployeeEntity employee);

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public List<EmployeeEntity> retrieveAllEmployee();
}
