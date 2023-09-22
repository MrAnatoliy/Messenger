import {MainPage} from "./pages/MainPage.tsx";
import {Route, Routes} from "react-router-dom";
import {Login} from "./pages/Login.tsx";
import {Register} from "./pages/Register.tsx";

const ROLES = {
    'User' : "user",
    'Editor' : "editor",
    'Admin' : "admin"
}

function App() {

  return (
    <Routes>

        {/* public routes */}
        <Route path="/home" element={<MainPage/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/signup" element={<Register/>}/>

        {/* private routes */}
        

        {/* catch all */}


    </Routes>
  )
}

export default App
