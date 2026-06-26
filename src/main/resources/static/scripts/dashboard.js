function abrirModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.classList.remove('hidden');
    }
}

function cerrarModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.classList.add('hidden');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.btn-open-modal').forEach(function (btn) {
        btn.addEventListener('click', function () {
            abrirModal(btn.getAttribute('data-modal-target'));
        });
    });

    document.querySelectorAll('[data-modal-close]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            cerrarModal(btn.getAttribute('data-modal-close'));
        });
    });
});
