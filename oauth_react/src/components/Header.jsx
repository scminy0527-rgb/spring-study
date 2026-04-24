import { Link } from "react-router-dom";
import { headerStyle } from "./Header.styles";
import useAuthStore from "../store/useAuthStore";

function Header() {
  const { isAuthenticated } = useAuthStore();

  return (
    <header style={headerStyle}>
      {!isAuthenticated && (
        <>
          <Link to="/login">로그인</Link>
          <Link to="/">회원가입</Link>
        </>
      )}
    </header>
  );
}

export default Header;
