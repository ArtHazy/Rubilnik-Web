export const App = () => {
  if (JSON.parse(localStorage.getItem('self-user'))) {
    return (
      <div className={"App"}>
        
      </div>
    );
  } else {
    {window.location.href = "/login"}
  }
}

export default App;
