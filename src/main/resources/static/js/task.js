let   editTaskModal = document.getElementById('editTaskModal');
let   needTaskUpdate  = false;

editTaskModal.addEventListener('show.bs.modal', function (event) {
    const $url            = '/task';

    const $message        = $('#messageTaskModal');
    const $errors         = $('#errorsTaskModal');

    const $position       = $('#positionTaskEdit');
    const $groupId        = $('#groupIdTaskEdit');

    const $saveBtn        = $('#saveBtnTaskModal');
    const $deleteBtn      = $('#deleteBtnTaskModal');
    const $dataForm       = $('#formTaskModal');

    const title           = editTaskModal.querySelector('.modal-title');
    const bsData          = event.relatedTarget.getAttribute('data-bs-whatever');

    setDefaultDataToModal(bsData);

    function insertPositionToElement(selectElm, count){
        selectElm.html("");

        if (count === 0){
            selectElm.append('<option value="1">1</option>');
        } else {
            for (let i = 1; i < count + 1; i++) {
                selectElm.append('<option value="' + i + '">' + i + '</option>');
            }
        }
    }

    function setDefaultDataToModal(data){
        if (data == null) {
            return;
        }

        let array = data.split('|');
        console.log(array);
        insertPositionToElement($position, parseInt(array[1]));

        if (array.length === 2){
            setNewDataToModal(array[0]);
            if (array[1] !== '0') {
                $position.append('<option value="0">to end</option>');
            }
            return;
        }

        let id = parseInt(array[2]);
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

    function setNewDataToModal(groupId) {
        let $name        = $('#nameTaskEdit');
        let $description = $('#descriptionTaskEdit');
        let $dateCreate  = $('#dateCreateTaskEdit');

        title.textContent = 'Add new task';
        $name.val("");
        $position.val(1);
        $dateCreate.val("");
        $description.val("");

        $groupId.val(groupId);
        $groupId.attr('disabled', true);

        $deleteBtn.hide();
        $message.hide();
        $errors.hide();
    }

    function setDataToModal(entity) {
        let $id          = $('#idTaskEdit');
        let $name        = $('#nameTaskEdit');
        let $description = $('#descriptionTaskEdit');
        let $dateCreate  = $('#dateCreateTaskEdit');

        title.textContent = 'Edit task by id:' + entity.id;
        $id.val(entity.id);
        $name.val(entity.name);
        $position.val(entity.position);
        $dateCreate.val(entity.dateCreate);
        $description.val(entity.description);

        $groupId.val(entity.groupId);
        $groupId.attr('disabled', false);

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
            $message.html('Task by id ' + data.id + 'saved');
            $message.show();
            needTaskUpdate = true;
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
            $message.html('Task successful deleted');
            $message.show();
            needTaskUpdate = true;
        }).fail(function (errors) {
            $saveBtn.attr('disabled', false);
            $deleteBtn.attr('disabled', false);

            $errors.html(errors.responseText);
            $errors.show();
        });
    }

    $saveBtn.on('click', function (event) {
        event.preventDefault();
        let state = $groupId.is(':disabled');
        $groupId.attr('disabled', false);
        let data = $dataForm.serialize();
        save(data);
        $groupId.attr('disabled', state);
    });

    $deleteBtn.on('click', function (event) {
        event.preventDefault();
        let state = $groupId.is(':disabled');
        $groupId.attr('disabled', false);
        let data = $dataForm.serialize();
        del(data);
        $groupId.attr('disabled', state);
    });
})

editTaskModal.addEventListener('hide.bs.modal', function () {
    if (needTaskUpdate) {
        location.replace(location.pathname);
    }
})