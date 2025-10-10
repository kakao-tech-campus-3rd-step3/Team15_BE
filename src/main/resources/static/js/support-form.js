const API = '/api/admin/support-links';
const id = new URLSearchParams(location.search).get('id');

if (id) {
    document.getElementById('pageTitle').innerText = '지원 사업 수정';
    loadData();
}

function toLocalInput(dt) {
    if (!dt) return '';
    const d = new Date(dt);
    const z = n => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${z(d.getMonth()+1)}-${z(d.getDate())}T${z(d.getHours())}:${z(d.getMinutes())}`;
}

async function loadData() {
    try {
        const res = await fetch(`/api/support-programs/${id}`);
        if (!res.ok) {
            alert('데이터 조회에 실패했습니다.');
            return;
        }
        const d = await res.json();

        setVal('name', d.name);
        setVal('company', d.company);
        setVal('content', d.content);
        setVal('place', d.place);
        setVal('applicationUrl', d.applicationUrl);
        setVal('imageUrl', d.imageUrl);
        setVal('supportType', d.supportType);
        document.getElementById('status').value = d.status || 'DRAFT';
        document.getElementById('startDate').value = toLocalInput(d.startDate);
        document.getElementById('endDate').value = toLocalInput(d.endDate);
    } catch (error) {
        console.error('로드 에러:', error);
        alert('데이터를 불러오는 중 오류가 발생했습니다.');
    }
}

function setVal(id, v) {
    const el = document.getElementById(id);
    if (el) el.value = v ?? '';
}

async function submitForm(e) {
    e.preventDefault();

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.disabled = true;
    submitBtn.textContent = '저장 중...';

    const body = {
        name: v('name'),
        company: v('company'),
        content: v('content'),
        place: v('place'),
        startDate: toISO(v('startDate')),
        endDate: toISO(v('endDate')),
        supportType: v('supportType'),
        applicationUrl: v('applicationUrl'),
        imageUrl: v('imageUrl'),
        status: v('status')
    };

    // 날짜 유효성 검사
    if (new Date(body.startDate) > new Date(body.endDate)) {
        alert('신청 시작일이 마감일보다 늦을 수 없습니다.');
        submitBtn.disabled = false;
        submitBtn.textContent = '💾 저장';
        return;
    }

    const method = id ? 'PATCH' : 'POST';
    const url = id ? `${API}/${id}` : API;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            alert('✅ 저장되었습니다.');
            location.href = '/admin/supports/list.html';
        } else {
            const err = await res.json().catch(() => ({}));
            alert('❌ 저장 실패: ' + (err.message || res.status));
            submitBtn.disabled = false;
            submitBtn.textContent = '💾 저장';
        }
    } catch (error) {
        console.error('저장 에러:', error);
        alert('❌ 저장 중 오류가 발생했습니다.');
        submitBtn.disabled = false;
        submitBtn.textContent = '💾 저장';
    }
}

function v(id) {
    return document.getElementById(id).value.trim();
}

function toISO(local) {
    return local ? new Date(local).toISOString() : null;
}
