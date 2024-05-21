const getUser = async () => {
    const table = document.querySelector('#tableBody');
    const span = document.querySelector('#span');
    const response = await fetch("/api/user");
    const user = await response.json();
    const tr = `<tr>
                  <td>${user.id}</td>
                  <td>${user.username}</td>
                  <td>${user.name}</td>
                  <td>${user.age}</td>
                  <td>${user.email}</td>
                  <td>${user.roles.map(role => role.valueOfRole)}</td>
                </tr>`
    table.innerHTML = tr;

    const newData = `<h5><b>${user.email}</b> with roles: ${user.roles.map(role => role.valueOfRole)}</h5>`;
    span.innerHTML = newData;

    const roles = `${user.roles.map(role => role.valueOfRole)}`

    if (!roles.includes("ADMIN")) {
        document.querySelector('#admin').style.display = "none";
    }
}


getUser();