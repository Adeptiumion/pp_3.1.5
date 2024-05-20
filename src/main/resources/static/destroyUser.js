const formDeleteUser = document.querySelector('#deleteForm')


const deleteModal = async (id) => {
    const response = await fetch(`http://localhost:8080/api/admin/get_user_by_id?id=${id}`);
    const user = await response.json();
    getDestroyModalInfo(user); 
}

const getDeleteFormItemByName = (name) => {
    console.log(formDeleteUser[name], "formDeleteUser[name]")
    return formDeleteUser[name];
}


const closeDeleteModal = () => {
    const deleteModalClose = document.querySelector('#deleteModalClose')
    deleteModalClose.click();
}

const getDestroyModalInfo = ({id, name, lastName, age, email, password}) => {
    getDeleteFormItemByName("idDelete").value = id;
    getDeleteFormItemByName("usernameDelete").value = name;
    getDeleteFormItemByName("lastNameDelete").value = lastName;
    getDeleteFormItemByName("ageDelete").value = age;
    getDeleteFormItemByName("emailDelete").value = email;
    getDeleteFormItemByName("passwordDelete").value = password;
}


formDeleteUser.addEventListener('submit', async (event) => {
    event.preventDefault()
    const response = await fetch(`http://localhost:8080/api/admin/delete?id=${getDeleteFormItemByName("idDelete").value}`, {
        method: 'DELETE'
    })
    getAllUsers();
    closeDeleteModal();
})