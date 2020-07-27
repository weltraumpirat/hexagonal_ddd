import React, {useState} from 'react'

export type InputEvent<T> = React.ChangeEvent<HTMLInputElement> & CustomEvent
export type Binding<T> = {
  value: T
  onChange: (e: InputEvent<T>) => void
}
export type InputBindings<T> = {
  value: T
  setValue: (v: T) => void
  reset: () => void
  bind: Binding<T>
}

export function useInput<T>(initialValue: T): InputBindings<T> {
  const [value, setValue] = useState(initialValue)

  return {
    value,
    setValue,
    reset: () => setValue(initialValue),
    bind: {
      value,
      onChange: (event: InputEvent<T>) => {
        setValue(event.target.value as unknown as T)
      }
    }
  }
}
