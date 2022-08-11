let   editGroupModal = document.getElementById('editGroupModal');
let   needGroupUpdate  = false;

editGroupModal.addEventListener('show.bs.modal', function (event) {
    const $url            = '/group';

    const $message        = $('#messageGroupModal');
    const $errors         = $('#errorsGroupModal');
    const $position       = $('#positionGroupEdit');

    const $saveBtn        = $('#saveBtnGroupModal');
    const $deleteBtn      = $('#deleteBtnGroupModal');
    const $dataForm       = $('#formGroupModal');

    const title           = editGroupModal.querySelector('.modal-title');
    const bsData          = event.relatedTarget.getAttribute('data-bs-whatever');

    setDefaultDataToModal(bsData);

    function setDefaultDataToModal(id){
        if (id == null) {
            setNewDataToModal();
            return;
        }

        $.ajax({
            url : $url + '/' + id,
            type: 'GET',
            beforeSend: function () {
                $message.hide();
                $errors.hide();
                $saveBtn.attr('disabled', true);
                $deleteBtn.attr('disabled', true);
            },
            complete: function () {
                $saveBtn.attr('disabled', false);
                $deleteBtn.attr('disabled', false);
            }
        }).done(function (entity) {
            setDataToModal(entity);
        }).fail(function (errors) {
            $errors.text(errors.responseText);
            $errors.show();
        });
    }

    function setNewDataToModal() {
        let $name     = $('#nameGroupEdit');

        title.textContent = 'Add new group';
        $name.val("");

        if ($position.find('option').length > 1) {
            $position.append('<option value="0">to end</option>');
        }

        $deleteBtn.hide();
        $message.hide();
        $errors.hide();
    }

    function setDataToModal(entity) {
        let $id       = $('#idGroupEdit');
        let $name     = $('#nameGroupEdit');

        title.textContent = 'Edit group by id:' + entity.id;
        $id.val(entity.id);
        $name.val(entity.name);

        $position.val(entity.position);
        $position.find('[value="0"]').remove();

        $deleteBtn.show();
    }

    function save(data) {
        $.ajax({
            url : $url,
            type: 'POST',
            data: data,
            beforeSend: function () {
                $message.hide();
                $errors.hide();
                $saveBtn.attr('disabled', true);
                $deleteBtn.attr('disabled', true);
            },
            complete: function () {
                $saveBtn.attr('disabled', false);
                $deleteBtn.attr('disabled', false);
            }
        }).done(function (data) {
            $message.html('Group by id ' + data.id + 'saved');
            $message.show();
            needGroupUpdate = true;
        }).fail(function (errors) {
            $errors.html(errors.responseText);
            $errors.show();
        });
    }

    function del(data) {
        $.ajax({
            url : $url,
            type: 'DELETE',
            data: data,
            beforeSend: function () {
                $saveBtn.attr('disabled', true);
                $deleteBtn.attr('disabled', true);
            },
        }).done(function () {
            $message.html('Group successful deleted');
            $message.show();
            needGroupUpdate = true;
        }).fail(function (errors) {
            $saveBtn.attr('disabled', false);
            $deleteBtn.attr('disabled', false);

            $errors.html(errors.responseText);
            $errors.show();
        });
    }

    $saveBtn.on('click', function (event) {
        event.preventDefault();
        let data = $dataForm.serialize();
        save(data);
    });

    $deleteBtn.on('click', function (event) {
        event.preventDefault();
        let data = $dataForm.serialize();
        del(data);
    });
})

editGroupModal.addEventListener('hide.bs.modal', function () {
    if (needGroupUpdate) {
        location.replace(location.pathname);
    }
})