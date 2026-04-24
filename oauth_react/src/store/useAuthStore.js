import { create } from "zustand";
import { combine, persist } from "zustand/middleware";

const useAuthStore = create(
  persist(
    combine(
      // state: 전역으로 관리할 상태 초기값
      {
        member: null,
        isAuthenticated: false,
        // localStorage에서 상태를 불러오는 작업(하이드레이션)이 끝났는지 여부
        // 앱 최초 로드 시 false → 하이드레이션 완료 후 true로 변경됨
        _hasHydrated: false,
      },
      // setter: 상태를 변경하는 함수 모음
      (set) => ({
        setMember: (member) => set({ member }),
        setIsAuthenticated: (status) => set({ isAuthenticated: status }),
        // 하이드레이션 완료 시 호출할 setter
        setHasHydrated: (state) => set({ _hasHydrated: state }),
      }),
    ),
    {
      name: "auth-store",

      // partialize: localStorage에 저장할 항목만 선택
      // _hasHydrated는 앱 실행마다 새로 계산해야 하므로 저장 대상에서 제외
      partialize: (state) => ({
        isAuthenticated: state.isAuthenticated,
        member: state.member,
      }),

      // onRehydrateStorage: localStorage에서 상태 복원이 끝난 직후 실행되는 콜백
      // 여기서 _hasHydrated를 true로 바꿔야 Layout들이 정확한 인증 상태를 볼 수 있음
      onRehydrateStorage: () => (state) => {
        state.setHasHydrated(true);
      },
    },
  ),
);

export default useAuthStore;
