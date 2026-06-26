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
    document.querySelectorAll('.btn-open-modal, [data-modal-target]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            abrirModal(btn.getAttribute('data-modal-target'));
        });
    });

    document.querySelectorAll('[data-modal-close]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            cerrarModal(btn.getAttribute('data-modal-close'));
        });
    });

    document.querySelectorAll('.modal-overlay').forEach(function (overlay) {
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) {
                overlay.classList.add('hidden');
            }
        });
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay:not(.hidden)').forEach(function (modal) {
                modal.classList.add('hidden');
            });
        }
    });

    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!confirm(form.getAttribute('data-confirm'))) {
                e.preventDefault();
            }
        });
    });

    var fechaHora = document.getElementById('fechaHora');
    if (fechaHora) {
        var now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        fechaHora.min = now.toISOString().slice(0, 16);
    }

    var btnValidarDni = document.getElementById('btnValidarDni');
    if (btnValidarDni) {
        btnValidarDni.addEventListener('click', validarDniReniec);
    }
});

/**
 * RF02 — Consulta DNI vía /api/admision/pacientes/{dni}/validar
 * Con perfil stub: 12345678, 87654321, 11223344
 * Con perfil prod + APIPERU_TOKEN: DNI real de RENIEC
 */
async function validarDniReniec() {
    var dniInput = document.getElementById('dniRegistro');
    var msg = document.getElementById('dniValidacionMsg');
    if (!dniInput || !msg) return;

    var dni = dniInput.value.trim();
    if (dni.length !== 8) {
        msg.textContent = 'Ingrese un DNI válido de 8 dígitos.';
        msg.className = 'field-hint text-danger';
        msg.classList.remove('hidden');
        return;
    }

    msg.textContent = 'Consultando RENIEC...';
    msg.className = 'field-hint';
    msg.classList.remove('hidden');

    try {
        var res = await fetch('/api/admision/pacientes/' + dni + '/validar');
        var data = await res.json();

        if (!res.ok) {
            var errorMsg = data.error || data.message || 'No se pudo validar el DNI.';
            throw new Error(errorMsg);
        }

        var nombresEl = document.getElementById('nombres');
        var apellidosEl = document.getElementById('apellidos');
        var fechaEl = document.getElementById('fechaNacimiento');
        var direccionEl = document.getElementById('direccion');

        if (nombresEl) nombresEl.value = data.nombres || '';
        if (apellidosEl) apellidosEl.value = data.apellidos || '';
        if (fechaEl && data.fechaNacimiento) fechaEl.value = data.fechaNacimiento;
        if (direccionEl && data.direccion) direccionEl.value = data.direccion;

        msg.textContent = 'Datos obtenidos correctamente (' + (data.nombres || '') + ' ' + (data.apellidos || '') + ').';
        msg.className = 'field-hint text-success';
    } catch (e) {
        msg.textContent = e.message || 'Error al consultar RENIEC.';
        msg.className = 'field-hint text-danger';
    }
}
