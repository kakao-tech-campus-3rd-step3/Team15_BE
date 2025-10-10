const API = '/api/admin/support-links';
let allData = [];

async function loadSupports() {
    try {
        const res = await fetch('/api/support-programs');
        if (!res.ok) throw new Error('데이터 로드 실패');

        allData = await res.json();
        updateStats(allData);
        filterAndRender();
    } catch (error) {
        console.error('로드 에러:', error);
        document.getElementById('tbody').innerHTML =
            '<tr><td colspan="6" class="message">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>';
    }
}

function updateStats(data) {
    document.getElementById('totalCount').textContent = data.length;
    document.getElementById('publishedCount').textContent =
        data.filter(r => r.status === 'PUBLISHED').length;
    document.getElementById('draftCount').textContent =
        data.filter(r => r.status === 'DRAFT').length;
    document.getElementById('closedCount').textContent =
        data.filter(r => r.status === 'CLOSED').length;
}

function filterAndRender() {
    const query = document.getElementById('search').value.trim().toLowerCase();
    const status = document.getElementById('statusFilter').value;

    let filtered = allData;

    if (query) {
        filtered = filtered.filter(r =>
            (r.name || '').toLowerCase().includes(query) ||
            (r.company || '').toLowerCase().includes(query)
        );
    }

    if (status) {
        filtered = filtered.filter(r => r.status === status);
    }

    renderTable(filtered);
}

function renderTable(data) {
    const tbody = document.getElementById('tbody');

    if (!data.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="message">검색 결과가 없습니다.</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(r => `
        <tr>
            <td>${r.id ?? ''}</td>
            <td>
                <a href="/admin/supports/detail.html?id=${r.id}">
                    ${escapeHtml(r.name ?? '')}
                </a>
            </td>
            <td>${escapeHtml(r.company ?? '')}</td>
            <td><span class="date-range">${dateRange(r.startDate, r.endDate)}</span></td>
            <td><span class="badge ${r.status}">${getStatusText(r.status)}</span></td>
            <td>
                <button class="btn-small btn-edit" onclick="location.href='/admin/supports/form.html?id=${r.id}'">
                    수정
                </button>
                <button class="btn-small btn-delete" onclick="deleteSupport(${r.id})">
                    삭제
                </button>
            </td>
        </tr>
    `).join('');
}

async function deleteSupport(id) {
    if (!confirm('정말 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) return;

    try {
        const res = await fetch(`${API}/${id}`, { method: 'DELETE' });

        if (res.status === 204 || res.ok) {
            alert('삭제되었습니다.');
            loadSupports();
        } else {
            const body = await res.json().catch(() => ({}));
            alert('삭제 실패: ' + (body?.message || res.status));
        }
    } catch (error) {
        console.error('삭제 에러:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
}

function getStatusText(status) {
    const statusMap = {
        'PUBLISHED': '게시됨',
        'DRAFT': '임시저장',
        'CLOSED': '종료'
    };
    return statusMap[status] || status || '';
}

function dateRange(s, e) {
    if (!s && !e) return '-';
    const start = s ? fmt(s) : '';
    const end = e ? fmt(e) : '';
    if (start && end) return `${start} ~ ${end}`;
    return start || end || '-';
}

function fmt(x) {
    if (!x) return '';
    return new Date(x).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

function escapeHtml(s) {
    if (!s) return '';
    return String(s).replace(/[&<>"']/g, m => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[m]));
}

// 검색 입력 시 엔터키로 검색
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('search').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') loadSupports();
    });

    // 초기 로드
    loadSupports();
});
