fetch('/api/spaces')
    .then(response => response.json())
    .then(spaces => {
        const container = document.getElementById('space-list');
        const countText = document.getElementById('count-text');

        if (spaces.length === 0) {
            countText.textContent = '등록된 공간이 없습니다';
            container.innerHTML = '<div class="empty-state">아직 저장된 스터디 공간이 없어요</div>';
            return;
        }

        countText.textContent = `총 ${spaces.length}곳`;

        spaces.forEach((space, index) => {
            const card = document.createElement('div');
            card.className = 'card';

            const num = String(index + 1).padStart(3, '0');
            const address = space.roadAddress || space.address || '주소 정보 없음';
            const phone = space.phone || '전화번호 미등록';

            card.innerHTML = `
        <div class="card-index">NO. ${num}</div>
        <h3>${space.name}</h3>
        <p>${address}</p>
        <p class="phone">${phone}</p>
        <a class="map-link" href="${space.placeUrl}" target="_blank">카카오맵에서 보기 →</a>
      `;
            container.appendChild(card);
        });
    })
    .catch(error => {
        console.error('공간 목록 불러오기 실패:', error);
        document.getElementById('count-text').textContent = '불러오기에 실패했습니다';
    });