const formDeleteUser = document.querySelector('#deleteForm')


const deleteModal = async (id) => {
    const response = await fetch(`/api/admin/${id}`);
    const user = await response.json();
    console.log("response user", user)
    getDeleteModelInfo(user);
}

const getDeleteFormItemByName = (name) => {

    console.log("node formDeleteUser", formDeleteUser[name])
    return formDeleteUser[name];
}


const closeDeleteModal = () => {
    const deleteModalClose = document.querySelector('#deleteModalClose')
    deleteModalClose.click();
}

const getDeleteModelInfo = ({id, name, lastName, age, email, password}) => {
    getDeleteFormItemByName("idDelete").value = id;
    getDeleteFormItemByName("usernameDelete").value = lastName;
    getDeleteFormItemByName("lastNameDelete").value = name;
    getDeleteFormItemByName("ageDelete").value = age;
    getDeleteFormItemByName("emailDelete").value = email;
    getDeleteFormItemByName("passwordDelete").value = password;
}


formDeleteUser.addEventListener('submit', async (event) => {
    event.preventDefault();
    const response = await fetch(`/api/admin/delete?id=${getDeleteFormItemByName("idDelete").value}`, {
        method: 'DELETE'
    })
    getAllUsers();
    closeDeleteModal();
})