import logo from './logo.svg';
import './App.css';
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {setJwt} from "./store/jwtSlice";

function App() {

  const jwt = useSelector((state) => state.jwt.value)
  const dispatch = useDispatch()

  useEffect(() => {

    const requestBody = {
      'email' : 'a.a@gmail.com',
      'password' : 'a'
    }

    fetch('api/auth/login', {
      headers:{
        'Content-Type': 'application/json'
      },
      method: 'POST',
      body: JSON.stringify(requestBody)
    })
        .then((response) => Promise.all([response.json(), response.headers]))
        .then(([body, headers]) =>
            dispatch(setJwt(body.token))
        );

  }, [dispatch]);



  return (
    <div className="App">
      <p>{jwt.payload}</p>
    </div>
  );
}

export default App;
