
// Функция принимающая по api залогиненного юзверя.
const getAuthorizeUser = async () => {
    try {
        const response = await fetch("http://localhost:8080/api/user"); // Ожидание исполнение и "непереход" книжней части тела функции(аналог '.then', ).
        const user = await response.json(); // Такое же ожидание, что и выше(дальше это комментировать не буду). Получаю респонс/преобразую в JSON.
        const userDescription = document.querySelector('#span'); // Захватываю элемент дом-дерева.
        userDescription.innerHTML = setAuthorizeUser(user); // Передаю в функцию данные юзверя, чтобы сформировать строку верстки. Монтирую возращаемую верстку в тот элемент, который я нашел при помощи querySelector.
    } catch(err) {
        console.error(err);
    }
}

const setAuthorizeUser = (user) => {
    return `<h5><b>${user.email}</b> with roles: ${user.roles.map(role => role.valueOfRole)}</h5>`;    
}

// Функия принимающая данные api, а именно список юзверей.
const getAllUsers = async () => {
    try {
        const table = document.querySelector('#tbody');
        const usersPool = await fetch("http://localhost:8080/api/admin/index");
        const preparedUsers = await usersPool.json();
        let rowsOfUsers = '';
        for (let user of preparedUsers) {
            rowsOfUsers += setColumnOfUser(user);
        }
        table.innerHTML = rowsOfUsers;
    } catch(e) {
        console.error(e);
    }
    
}

const setColumnOfUser = (user) => {
    return `
    <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.lastName}</td>
        <td>${user.age}</td>
        <td>${user.email}</td>
        <td>${user.roles.map(role => role.valueOfRole)}</td>
        <td> 
            <button  type="button" class="btn btn-primary" data-toggle="modal" data-target="#editModal" onclick="editModal(${user.id})">Edit</button>
        </td>
        <td>
            <a  style="color: white" class="btn btn-danger" data-target="#deleteModal" data-toggle="modal" onclick="deleteModal(${user.id})">delete</a>
        </td>
    </tr>
    `
}


const userInit = async () => {
    // Сначала получим авторизованного юзверя.
    await getAuthorizeUser();
    // А уже потом по 'левым типам'.
    await getAllUsers();
}

userInit();







