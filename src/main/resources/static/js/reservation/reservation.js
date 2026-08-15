// /static/js/reservation/reservation.js

function reserveSlot(timeSlotId, btnEl) {
    if (btnEl.disabled) return;

    btnEl.disabled = true;
    const originalText = btnEl.textContent;
    btnEl.textContent = "예약 중...";

    fetch('/api/reservations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ timeSlotId: Number(timeSlotId) })
    })
        .then(async (res) => {
            if (res.ok) {
                alert('예약이 완료되었습니다.');
                location.reload();
                return;
            }

            const err = await res.json();
            alert(err.message || '예약에 실패했습니다.');
            btnEl.disabled = false;
            btnEl.textContent = originalText;
        })
        .catch(() => {
            alert('서버와 통신 중 문제가 발생했습니다.');
            btnEl.disabled = false;
            btnEl.textContent = originalText;
        });
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.slot-btn[data-slot-id]').forEach((btn) => {
        btn.addEventListener('click', () => reserveSlot(btn.dataset.slotId, btn));
    });
});